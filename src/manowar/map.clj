(ns manowar.map)


;;; To make this work in higher-noon engine just use these as keys and map it to coordinates on screen
(defn clean-map [radius]
  (reduce concat  (for [i (map inc  (range radius))]
                    (for [j (map inc (range (* radius i)))]
                      [i j]))))

;;; generate a map with opstacles such as asteroids, ion-nebulas, and planets/stars
