(ns imaje.handler
  (:use [imaje.processing]
        [imaje.filters]
        [compojure.core]
        [hiccup.core]
        [ring.middleware.params]
        [ring.middleware.multipart-params])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]))

(defn home-page []
  (html
    [:form {:action "/filter" :method "post" :enctype "multipart/form-data"}
      [:input {:name "file" :type "file"}]
      [:div
        [:input
          {:name "filter" :type "radio" :value "invert" :checked true}
          "Invert"]
        [:input
          {:name "filter" :type "radio" :value "desaturate" :checked false}
          "Desaturate"]]
      [:input {:type "submit" :name "submit" :value "Filter"}]]))

(defn upload-file [file filter]
  (save-image (filter-image (load-image file) (image-filters (keyword filter)))))

(defroutes app-routes
  (GET "/" [] (home-page))
  (wrap-multipart-params
    (POST "/filter" {params :params}
      (upload-file (get params :file) (get params :filter))))
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
