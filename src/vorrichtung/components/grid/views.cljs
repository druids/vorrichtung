(ns vorrichtung.components.grid.views
  (:require
    [clojure.string :refer [join]]
    [re-frame.core :refer [subscribe dispatch]]
    [vorrichtung.components.grid.db :refer [config->order-class order->order-column-map item-value]]
    [vorrichtung.html :refer [safe-html]]
    [vorrichtung.utils :refer [css-class]]))


(def ordering->class
  {:ASC "sorted"
   :DESC "sorted-reverse"
   :NONE nil})


(defn th
  [config column column-order-index]
  [:th {:class (:id column)}
   (if (:orderable_by column)
     [:div
      {:on-click #(dispatch [:grid/order-by-column config (:orderable_by column)])}
      [:span {:class (css-class ["sortable" (config->order-class config (:orderable_by column) ordering->class)])}
       (:verbose_name column)]
      [:div.sort-direction]]
     (:verbose_name column))])


(defn thead
  [config order-column-map th]
  [:thead
   [:tr
    (for [column (:columns config)]
      ^{:key (:id column)}
      [th config column (order-column-map (:orderable_by column))])]])


(defn mark-value-as-safe
  [column item]
  (let [value (item-value item column)]
    (if (:safe_html column)
      (safe-html value)
      value)))


(defn default-action-or-nil
  [item]
  (get-in item [:_web_links (keyword (:_default_action item))]))


(defn td
  [column item]
  [:td
   (let [safe-value (mark-value-as-safe column item)
         default-action (default-action-or-nil item)]
     (if default-action
       [:a {:href default-action} safe-value]
       safe-value))])


(defn tr
  [config item td]
  [:tr {:class (join " " (:_class_names item))}
   (for [column (:columns config)]
     ^{:key (str (:id item) "__" (:id column))}
     [td column item])])


(defn tbody
  [config data tr td]
  [:tbody
   (for [item data]
     ^{:key (:id item)} [tr config item td])])


(defn table
  [grid-data thead th tbody tr td]
  [:table
   [thead (:config grid-data) (order->order-column-map (get-in grid-data [:config :order])) th]
   [tbody (:config grid-data) (:data grid-data) tr td]])


(defn empty-grid
  []
  [:div "No items"])
