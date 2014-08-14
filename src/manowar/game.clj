(ns manowar.game
  (:require [manowar.ships :refer :all]
            [manowar.map :refer :all]
            [manowar.ai :as ai]))

;get the adjacent spaces, see which one within movement speed brings you closest to destination.


;;; This will look at all the adjacent points, and see which ones shortens the distance to destination
(defn plot-course
  "This a list of the coordinates that will be visited, based on your currents speed, given your position and course."
  [ship]
  (let [loc (ship :location)
        dest (ship :destination)
        adj (adjacent-points loc)
        distances (map distance (repeat (count adj) loc) adj)]
    (get  adj (.indexOf distances (apply min distances)))
    ;; this needs to get the rest of moves still, and to assoc them into course
    ))

(defn set-course [ship course]
  (assoc ship :course course ))

(defn set-speed [ship increment]
  (assoc ship :speed (+  (:speed ship) increment) ))

;;; this will update the location of the ship by looking at the speed and destination
(defn move-ship [ship]
  )
