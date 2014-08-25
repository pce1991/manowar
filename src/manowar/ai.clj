(ns manowar.ai
  (:require [manowar.game :refer :all]
))

;;; The AI needs to have its own knowledge distinct from the true game-state.

;;; should set a destination towards the initial position of the player. 
;;; Should not adjust destination until it has arrived (naive, but conservative
(defn set-destination-ai [ship enemy-pos] ;or should it be given the whole enemy ship?
  (set-destination ship enemy-pos))

;;; Should set speed as a random number between 1 and the max-speed
(defn set-speed-ai [ship]
  (set-speed ship (inc  (rand-int (:max-speed (:engine ship))))))

;;; Assume that the second ship is always the ai
(defn ai-turn [ships]
  (->
   (set-destination (ships 2) (:position (ships 1)))
   set-speed))
