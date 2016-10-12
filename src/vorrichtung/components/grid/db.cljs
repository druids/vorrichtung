(ns vorrichtung.components.grid.db
  (:require
    [clojure.string :refer [join split]]
    [goog.string :refer [format]]
    [goog.string.format]
    [linked.core :as linked]
    [vorrichtung.core :refer [get-value]]
    [vorrichtung.num :refer [str->int]]))


(defrecord ColumnOrder [id ordering])


(defn serialized-order->order
  "Takes a `serialized-order` string and converts it into an order object. E.g.:
    'foo' -> {:id 'foo'
              :ordering :ASC}
   '-foo' -> {:id 'foo'
              :ordering :DESC}"
  [serialized-order]
  (let [first-char (first serialized-order)]
    (if (= "-" first-char)
      (ColumnOrder. (subs serialized-order 1) :DESC)
      (ColumnOrder. serialized-order :ASC))))


(defn unserialize-order
  [order]
  "Takes a serialized `order` string and converts it into an internal structure. Unserialized order are kept in
   a linked-map for an easy key->value access and column ordering."
  (->> order
       (map serialized-order->order)
       (reduce (fn [order-map serialized-order] (assoc order-map (:id serialized-order) serialized-order))
               (linked/map))))


(defrecord Column
  [
   id ; a column ID
   verbose_name ; a humanized label
   orderable_by ; a column name that is used for ordering
   safe_html ; set to `false` if a `verbose-name` should not be escaped
   ])


(defrecord Config
  [
   id ; grid ID
   columns ; list of Column
   url ; an URL as a string
   order ; [ColumnOrder]
   serialization_format
   namespace ; a grids prefix, override when you want to change a path of grid's data
   total ; a total count of items
   offset ; an offset for pagination
   base ; a count of items per a page
   progress? ; `true` if a request for data is in progress
   ])


(defn args->config
  [el args]
  (Config. (keyword (.-id el))
           (map #(map->Column. %) (get-value args :columns))
           (get-value args :url)
           (unserialize-order (get-value args :order []))
           :VERBOSE
           (get-value args :namespace :grid)
           0
           0
           (get-value args :base 20)
           false))


(def default-fields
  [:id :_obj_name :_rest_links :_actions :_class_names :_web_links :_default_action])


(defn group-by-nested-field
  "Groups a nested field and returns an updated map.
   When a column `id` looks like `foo__name` returns:
   1. for an empty map {'foo' ['name']}
   2. for a map with an existing key {'foo' ['age' 'name']}

   For a column `id` 'foo' without a subfield returns {'foo' [nil]}"
  [group-by-columns column]
  (let [parts (split (:id column) #"__")]
    (assoc group-by-columns
           (first parts)
           (conj (get group-by-columns (first parts) []) (when (> (count parts) 1)
                                                           (second parts))))))


(defn format-grouped-fields
  "Unpack a given vector into a `prefix` and subfields.
   When `subfields` is non-empty collection returns a `string` like: 'foo(subfield1,subfield2)',
   otherwise returns `prefix`."
  [[prefix subfields]]
  (let [filtered-subfields (remove nil? subfields)]
    (if (seq filtered-subfields)
      (->> filtered-subfields
           (join ",")
           (format "%s(%s)" prefix))
      prefix)))


(defn flatten-formatted-nested-fields
  [columns]
  (->> columns
       (reduce group-by-nested-field {})
       (map format-grouped-fields)))


(defn config->header-fields
  [config]
  (->> (flatten-formatted-nested-fields (:columns config))
       (concat (map name default-fields))
       (join ",")))


(defn next-order-state
  "Return next ordering state. Ordering cycle follows:
   ASC -> DESC
   DESC -> NONE
   NONE -> ASC"
  [ordering-direction]
  (get {:ASC :DESC
        :DESC :NONE
        :NONE :ASC}
       ordering-direction
       :NONE))


(defn apply-next-state
  "Applies a new state of a given `column-order` on `order`."
  [order column-order]
  (let [change-column-order (fn [order column-order] (assoc order (:id column-order) column-order))
        remove-column-order (fn [order column-order] (dissoc order (:id column-order)))
        default (fn [order _] order)]
    (apply (get {:ASC change-column-order
                 :DESC change-column-order
                 :NONE remove-column-order}
                (:ordering column-order)
                default)
           [order column-order])))


(defn config->order-class
  "Return a CSS class as for a given `column-id`. `ordering->class` should be a `map` like:
   {:ASC 'ascending'
    :DESC 'descending'
    :NONE nil}"
  [config orderable-by ordering->class]
  (get ordering->class (get-in config [:order orderable-by :ordering])))



(defn column-order->serialized-order
  "Serializes a given `column-order` into a `string`. E.g.:
   {:id 'foo'
    :ordering :DESC} -> '-foo'"
  [[_ column-order]]
  (if (= (get column-order :ordering) :DESC)
    (str "-" (:id column-order))
    (str (:id column-order))))


(defn serialize-order
  "Converts an internal `order` structure into a `string`. See `unserialize-order` function."
  [order]
  (->> order
       (map column-order->serialized-order)
       (join ",")))


(defn default-column-order
  [column-name]
  (ColumnOrder. column-name :NONE))


(defn order->order-column-map
  "Converts `order` to column->index map like:
   {:a 'a'     {:a 1
    :b 'b'} ->  :b 2}
   Returned map provides an index order for ordered columns.
  "
  [order]
  (zipmap (keys order) (range 1 (inc (count order)))))


(defn compose-config-path
  [config]
  [(:namespace config) (:id config) :config])


(defn item-value
  "Return a value from `item` by `column`. A column name can be nested like `foo__name`."
  [item column]
  (->> (split (:id column) #"__")
       (map keyword)
       (get-in item)))


(defn headers->total
  [headers]
  (str->int (:X-Total headers)))


(defn previous-page?
  "Returns `true` if there is previous page, otherwise `false`."
  [config]
  (pos? (:offset config)))


(defn next-page?
  "Returns `true` if there is next page, otherwise `false`."
  [config]
  (pos? (reduce - (:total config) [(:offset config) (:base config)])))


(defn pos-or-zero
  [value]
  (if (pos? value)
    value
    0))


(defn shift-page
  "Shifts to previous/next by given `pred` and `op`. `pred` should be a function that returns `true`
   if `op` should be applied. `op` should be a function that takes `offset` and `base` and makes a computation.
   This function returns a function that takes a `config` and returns new one."
  [pred op]
  (fn [config]
    (update config :offset #(if (pred config)
                              (pos-or-zero (op % (:base config)))
                              %))))


(defn toggle-progress
  [db config value]
  (assoc-in db (conj (compose-config-path config) :progress?) value))
