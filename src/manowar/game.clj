(ns manowar.game
  (:require [manowar.ships :refer :all]
            [manowar.map :refer :all]
            [manowar.ai :as ai]))

;get the adjacent spaces, see which one within movement speed brings you closest to destination.


;;; This will look at all the adjacent points, and see which ones shortens the distance to destination
;;; this doesnt go in a straight enough line, tending to veer to the left when going up. Maybe I should correct 
;;; this by just drawing a line between two points and finding which point is closest the line.
;;; I should make sure that adjacent points is sorted? That would just prejudice
;;; the point above, and not necessarily solve the problem of veering in other cases
;;; I need a way to check, when I get indexOf, if there are multiple minimums,
;;; if there are, then I need a way to check which is the most desirable. 
(defn plot-course
  "This a list of the coordinates that will be visited, based on your currents speed, given your position and course."
  [ship]
  (let [loc (ship :location)
        dest (ship :destination)
        adj (adjacent-points loc)
        distances (map distance (repeat (count adj) dest) adj)]
    (loop [course [(get  adj (.indexOf distances (apply min distances)))]
                temp-loc (first course)]
      (if (= temp-loc dest)
        (assoc ship :course course)
        ;continue by getting distances with the new temp-loc then consing the (get adj (index ....))
        ;; make sure all this works
        (let [new-adj (adjacent-points temp-loc)
              new-distances (map distance (repeat (count new-adj) dest) new-adj)
              next-loc (get new-adj (.indexOf new-distances (apply min new-distances)))]
          (recur (conj course next-loc) next-loc))))))

(defn set-course [ship course]
  (assoc ship :course course ))

(defn set-speed [ship increment]
  (assoc ship :speed (+  (:speed ship) increment) ))

;;; this will update the location of the ship by looking at the speed and destination
(defn move-ship [ship]
  )
