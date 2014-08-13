(ns manowar.game
  (:require [manowar.ships :refer :all]
            [manowar.map :refer :all]
            [manowar.ai :as ai]))


(defn calculate-distance [p1 p2]
  (let [x1 (p1 0) 
        y1 (p1 1) 
        x2 (p2 0) 
        y2 (p2 1)]
    ))

;get the adjacent spaces, see which one within movement speed brings you closest to destination.

(defn plot-course 
  "This a list of the coordinates that will be visited, based on your currents speed, given your position and course."
  [ship])

(defn set-course [ship course]
  (assoc ship :course course ))

(defn set-speed [ship increment]
  (assoc ship :speed (+  (:speed ship) increment) ))

;;; this will update the location of the ship by looking at the speed and destination
(defn move-ship [ship]
  )
