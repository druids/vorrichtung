(ns vorrichtung.components.grid.views
  (:require
    [clojure.string :refer [join]]
    [re-frame.core :refer [subscribe dispatch]]
    [vorrichtung.components.grid.db :refer [config->order-class order->order-column-map item-value previous-page?
                                            next-page?]]
    [vorrichtung.html :refer [safe-html]]
    [vorrichtung.utils :refer [css-class]]))


(def ordering->class
  {:ASC "sorted"
   :DESC "sorted-reverse"
   :NONE nil})


(defn th
  [config column column-order-index mappings]
  [:th {:class (:id column)}
   (if (:orderable_by column)
     [:div
      {:on-click #(dispatch [(get-in mappings [:events :order-by-column]) config (:orderable_by column)])}
      [:span {:class (css-class ["sortable" (config->order-class config (:orderable_by column) ordering->class)])}
       (:verbose_name column)]
      [:div.sort-direction]]
     (:verbose_name column))])


(defn thead
  [config order-column-map th mappings]
  [:thead
   [:tr
    (for [column (:columns config)]
      ^{:key (:id column)}
      [th config column (order-column-map (:orderable_by column)) mappings])]])


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


(defn show-previous-button?
  [config]
  (and (previous-page? config) (not (:progress? config))))


(defn show-next-button?
 [config]
 (and (next-page? config) (not (:progress? config))))


(defn paginator
  [config mappings]
  [:div.paginator
   [:div {:on-click #(when (show-previous-button? config)
                       (dispatch [(get-in mappings [:events :go-to-previous-page]) config]))
          :class (if (show-previous-button? config) "enabled" "disabled")}
    "Previous"]
   [:div {:on-click #(when (show-next-button? config)
                       (dispatch [(get-in mappings [:events :go-to-next-page]) config]))
          :class (if (show-next-button? config) "enabled" "disabled")}
    "Next"]])


(defn table
  [grid-data thead th tbody tr td mappings]
  [:table
   [thead (:config grid-data) (order->order-column-map (get-in grid-data [:config :order])) th mappings]
   [tbody (:config grid-data) (:data grid-data) tr td]])


(defn empty-grid
  []
  [:div "No items"])
