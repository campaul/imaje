(ns imaje.filters)

(defn invert
  [[r g b]]
    [(- -1 r) (- -1 g) (- -1 b)])
     
(defn desaturate
  [[r g b]] [g g g])

(def image-filters {
  "invert" invert
  "desaturate" desaturate
})
