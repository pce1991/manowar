(ns manowar.game
  (:require [manowar.ships :refer :all]
            [manowar.map :refer :all]
;            [manowar.ai :as ai] ;circular dependency, AI will use game
            ))

;get the adjacent spaces, see which one within movement speed brings you closest to destination.


;;; This will look at all the adjacent points, and see which ones shortens the distance to destination
;;; this doesnt go in a straight enough line, tending to veer to the left when going up.
;;; I'll add a check here to see if a course exists, if not, then procede as normal, but if so, drain the engine
(defn plot-course
  "This a list of the coordinates that will be visited, based on your currents speed, given your position and course. Returns a ship with the updated course."
  [ship]
  (let [loc (ship :location)
        dest (ship :destination)
        adj (adjacent-points loc)
        distances (map distance (repeat (count adj) dest) adj)]
    (loop [course [(get  adj (.indexOf distances (apply min distances)))]
                temp-loc (first course)]
      (if (= temp-loc dest)
        (assoc ship :course course)
        (let [new-adj (adjacent-points temp-loc)
              new-distances (map distance (repeat (count new-adj) dest) new-adj)
              next-loc (get new-adj (.indexOf new-distances (apply min new-distances)))]
          (recur (conj course next-loc) next-loc))))))

(defn set-course [ship course]
  (assoc ship :course course))

(defn set-destination [ship dest]
  "Sets a destination on the ship and automatically updates course."
  (plot-course (assoc ship :destination dest)))

(defn set-speed [ship val]
  (let [v (if (< val (:max-speed (:engine ship)))
            val
            (:max-speed (:engine ship)))] 
    (assoc ship :engine (assoc (:engine ship) :speed-setting v))))

;;; This is where drain will come into effect, by taking the current speed after the ship has accelerated. 
;;; This also needs a cap, but on power!
;;; getting a null pointer here!
(defn accelerate [ship]
  "This will increase the current speed of the ship by the acceleration if the speed setting is currently >"
  (if (<   (:speed-current (:engine  ship)) (:speed-setting (:engine  ship)))
    (let [ship-speed-increased 
          (assoc ship :engine (assoc (:engine ship) :speed-current (+  (:speed-current  (:engine ship)) 
                                                                       (:acceleration (:engine ship)))))]
      (assoc ship-speed-increased :generator 
             (assoc (:generator ship-speed-increased) 
               :current-output (+ (:current-output (:generator ship-speed-increased))
                                  (* engine-drain (:speed-current (:engine ship-speed-increased)))))))
    ship))

;;; this will update the location of the ship by looking at the speed and destination
(defn move-ship [ship]
  "This will update the location of the ship from the first of the course, and shave that off."
  ;; this needs to be changed to a loop where it does this a number of times determined by their speed. 
  (if-not (nil? (:course ship)) 
    (loop [updated-ship ship
           speed (:speed-current (:engine ship))
           moves 0]
      (if (= moves speed)
        updated-ship 
        (recur 
         (assoc updated-ship :location (first (:course updated-ship)) 
                :course (rest (:course updated-ship)))
         speed (inc moves))))
    ship))


;;; ==================================================
;;; COMBAT
;;; ==================================================

(defn destroyed? [ship]
  (<= (:hull ship) 0))

