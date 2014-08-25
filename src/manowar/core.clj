(ns manowar.core
  (:require [clojure.tools.namespace.repl :refer :all]
            [manowar.game :refer :all]
            [manowar.ships :refer :all])
  
  (:gen-class))

;;; start the game with setting course. And your speed? 

;;; I'll get commands and stuff elsewhere, but this is the state that gets updated every ~5 seconds with map
;;; Do I worry about navigation commands here? 
(defn update-navigation [ships]
  "Returns a map the ships with updated speed and positions. Returns a single map so it can be used with threading."
  {1 (move-ship  (accelerate (ships 1))) 2 (move-ship (accelerate (ships 2)))})

(defn game-loop [ships]
  (if-not (some destroyed? ships)
    (->
     (update-navigation ships)
     
     )))

(defn initialize-game []
  (game-loop  (make-ship-pair)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
