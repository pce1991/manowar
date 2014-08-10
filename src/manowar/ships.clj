(def manowar.ships)

(def location-health 25)
(def charge-drain 5)

(def make-ship  
  {:name nil 
   :captain nil 
   :hull 100
   :shield 0
   :power 100                           ;this is calculated by (- max drain) in :generator
   :speed 0
   :course nil 
   ;; radomize the location of systems. Will take a key matching those in locations. Add more
   :systems {:computer nil 
             :engine nil
             :generator nil
             :shield nil
             :scanner nil }
   ;; include more locations
   :locations {:bow location-health
               :stern location-health
               :port location-health
               :starboard location-health
               :aft location-health}
   :scanner nil ; a read out of the map, so what the player sees and what the state is are separate.
   :generator {:max-power 100
               :charging nil ;if its charging this will be a number representing how long its been. 
               :drain 0}
   :computer {:active true
              :access nil
              :hacked nil}})

;;; Need to figure out path-finding if the course is imprecise. 
