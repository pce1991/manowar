(ns manowar.map)

(defn clean-map [radius]
  (reduce concat  (for [i (map inc  (range radius))]
                    (for [j (map inc (range (* radius i)))]
                      [i j]))))

