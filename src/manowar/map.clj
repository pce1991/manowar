(ns manowar.map
  (:require [clojure.math.numeric-tower :as math]))

;This exitst an a cartesian plane so that its easy to calculate distance between points. How to tie this to
;;; binary circle? How to tied it to the screen coordinates in higher-noon? It needs to be organized by ring
;;; make sure this is the same count as clean map... it isnt! its fine for 4, but off at radius = 8.
;;; Code problem, or did I misjudge how similar the grid and circle maps would be? Its off by 16.
;;; use grid-ring to find out what isnt matching up!
(defn grid-map [radius]
  (let [x-row (range (- radius) (inc  radius))
        y-row (reverse  (range (- radius) (inc  radius)))]
    (reduce concat  (for [i x-row] 
                      (for [j y-row]
                        [i j])))))

;;; To make this work in higher-noon engine just use these as keys and map it to coordinates on screen
;;; This should be changed to coordinates like on a graph instead of the binary circle that I'm using now.
(defn clean-map [radius]
  (conj  (reduce concat  (for [i (map inc  (range radius))]
                           (for [j (map inc (range (* radius i)))]
                             [i (dec  j)]))) [0 0]))


(def grid (grid-map 8)) ;game representation map
(def coordinates (clean-map 8)) ;player representation of map

(defn get-xs [vector-seq]
  (map first vector-seq))

(defn get-ys [vector-seq]
  (map second vector-seq))

(defn sort-clockwise 
  "This sorts a sequence of coordinate pairs starting at the 12 o'clock position and moving clockwise."
  [vector-seq]
  (let [start [0 (apply max (get-ys vector-seq))]]
    ;; make the right, down, left, up vectors here so its less repetition in code.
    (loop [new-seq (list start) 
           current start
           vectors (remove #{start}  vector-seq)]
      (cond (= (count new-seq) (count vector-seq)) 
            (reverse  new-seq)                 ;reversing this give me nil...?
            (some #{[(inc (current 0)) (current 1)]} vectors) ;if there's one to the right
            (recur (conj new-seq [(inc (current 0)) (current 1)]) [(inc (current 0)) (current 1)] 
                   (remove #{[(inc (current 0)) (current 1)]} vectors)
                   )
            (some #{[(current 0) (dec  (current 1))]} vectors) ;if there's one down
            (recur (conj new-seq [(current 0) (dec  (current 1))]) [(current 0) (dec  (current 1))] 
                   (remove #{[(current 0) (dec  (current 1))]} vectors)
                   )
            (some #{[(dec  (current 0)) (current 1)]} vectors) ;if there's one to the left
            (recur (conj new-seq [(dec  (current 0)) (current 1)]) [(dec  (current 0)) (current 1)] 
                   (remove #{[(dec  (current 0)) (current 1)]} vectors)
                   ) 
            (some #{[(current 0) (inc (current 1))]} vectors) ;if there's one above
            (recur (conj new-seq [(current 0) (inc (current 1))]) [(current 0) (inc (current 1))] 
                   (remove #{[(current 0) (inc (current 1))]} vectors)
                   )))))

;why does sort not work
(defn grid-ring [n]
  "Returns nth ring in a grid, sorted clockwise."
  (sort-clockwise  
   (filter (fn [[x y]] (= n (max (Math/abs x) (Math/abs y)))) grid )))

;;; pretty slow it seems, and it doesnt return them in the right order, which should be clockwise
(defn grid-rings 
  "Returns the coordinates for the rings in ascending order, essentially reorganizes the coordinate points."
  []
  (reduce concat   (for [i (range (inc  (apply max (get-ys grid))))]
                     (grid-ring i))))

(defn get-ring 
  "Retrieves the nth ring from coordinates."[n]
  (filter (fn [[x y]] (= x n))    coordinates))

;this will be done by taking the coordinates and mapping them to each ring of grid.
;;; why this no work?
(def player-coord-map
  (zipmap coordinates (grid-rings)))

(defn grid-coord->player-coord [grid-coord]
  ((clojure.set/map-invert player-coord-map) grid-coord))

;;; I'll need another map that takes player-coord and keys them to the screen coordinates of circles.
;;; To draw something based on its position look up the ship-location, then get its screen coord from
;;; the map.

;;; this will take the playercoord, look up the grid-coord, and calculate the distance with those values.
(defn distance [p1 p2]

  (math/round (math/sqrt  (reduce + (map * (map - p2 p1) (map - p2 p1))))))

;;; generate a map with opstacles such as asteroids, ion-nebulas, and planets/stars

;;; This should work, but it hasnt been tested sufficiently.
(defn adjacent-points [point]
  "Takes a point as a radial-coordinate and then returns a list of adjacent radial-coordinates."
  (let [grid-point (player-coord-map point)
        x (grid-point 0)
        y (grid-point 1)
        up [x (inc y)]
        up-right (mapv inc grid-point)
        right [(inc x) y]
        down-right [(inc x) (dec y)]
        down [x (dec y)]
        down-left (mapv dec grid-point)
        left [(dec x) y]
        up-left [(dec x) (inc y)]]
    (letfn [(adjacent-to-grid-point? [p] 
              (if  (some #{p} [up up-right right down-right down down-left left up-left])
                true 
                false)) ]
      ;now just need to convert these to 
      (mapv grid-coord->player-coord (filter adjacent-to-grid-point? (vals player-coord-map))))))

