(def manowar.ships)

(def location-health 25)
(def charge-drain 5)

(def weapons {:nuke nil 
              :lasers nil 
              :flak nil 
              :gauss nil
              :missiles nil})

(def ship  
  {:name nil 
   :captain nil 
   :hull 100
   :shield 0
   :power 100                           ;this is calculated by (- max drain) in :generator
   :speed 0
   :course nil 
   :location nil 
   ;; radomize the location of systems. Will take a key matching those in locations. Add more. 
   ;; there will be between 5 and 8 weapons, so have areas for those as well. 
   :systems {
             :computer nil              ;how to balance this? 
             :engine nil
             :generator nil
             :shield nil
             :scanner nil
             :drone-bay nil}
   ;; include more locations
   :locations {:bow location-health
               :stern location-health
               :port location-health
               :starboard location-health
               :aft location-health
               :forward location-health
               :waist location-health}

   :generator {:max-power 100
               :charging nil ;if its charging this will be a number representing how long its been. 
               :drain 0 ;this increases from sustaining shields, speed, and charging weapons.
               }
   :computer {:active true
              :access nil
              :hacked nil
              :scan nil ; a read out of the map, so what the player sees and what the state is are separate.
              :readout nil ;this is a readout of all systems, and may be incorrect if hacked. 
              }
              

   :drones {:deployed nil ;how many are deployed?
            :mode nil ;this will be recon, assault, or defense
            :position nil
            
            }})

(def drone {:hull 100
            :location nil
            :course nil})

(defn make-ship [])

;;; Need to figure out path-finding if the course is imprecise. 
