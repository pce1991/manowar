(def manowar.ships)

(def location-health 25)
(def charge-drain 5)

(def weapons {:nuke nil 
              :lasers nil 
              :flak nil 
              :cannon nil
              :gauss nil   ;maybe this is charged, the longer the stronger and further it goes. 
              :missiles nil})

(def drone {:hull 100
            :location nil
            :course nil ;maybe add a target setting so it can just follow the enemy ship
            :mode nil ;offense, defense, scout. Offense might be useless, maybe it should be a patrol watching 
            ;; out for missiles in the area or something. 
            })

(def manowar  
  {:name nil 
   :captain nil 
   :hull 100
   :shield 0
   :power 100                           ;this is calculated by (- max drain) in :generator
   :engine {:speed 0
            :max-speed 4
            :acceleration 2}
   :destination nil
   :course nil 
   :location nil 
   ;; radomize the location of systems. Will take a key matching those in locations. Add more. 
   ;; there will be between 5 and 8 weapons, so have areas for those as well. 
   :system-locations  {
             :engine nil
             :generator nil
             :shield nil                ;prevents them from raising their shields again.
             :scanner nil
             :drone-bay nil ;will cut contact to any active drones
             :weapons-bay-1 nil ;each bay has 3 weapon systems in it. 
             :weapons-bay-2 nil}
   ;; include more locations. 
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
              

   ;; replace this by drawing on the drone definition below. 
   :drones {:deployed nil ;how many are deployed?
            :mode nil ;this will be recon, assault, or defense
            :position nil
            
            }})

(defn assign-system-locations 
  "Returns a map of systems randomly mapped to locations" 
  []
  (zipmap 
   (keys (:system-locations manowar))
   (shuffle  (keys (:locations manowar)))))



;;; This'll assign systems to locations, name your ship, 
(defn make-ship [])

;;; Need to figure out path-finding if the course is imprecise. 

