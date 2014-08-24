(ns manowar.ships
  (:require [manowar.map :refer :all]))

(def location-health 25)
(def charge-drain 5)
(def engine-drain 5)
(def course-correction-drain 50)        ;this makes changing course quite expensive

;;; a weapon will have hull and shield damage, a cost, range, and speed. Some will have course/destination, charging?, and a firing mode amongst other specific parameters. 
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
   :engine {:speed-current 0
            :speed-setting 0
            :max-speed 4
            :acceleration 2}  ; add the cost per unit of speed
   :destination nil
   :course nil 
   :location nil 
   :weapon-bays {1 nil 2 nil}
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

   :generator { :max-output 100
               :current-output 0 ;this increases from sustaining shields, speed, and charging weapons, moving, etcetera
               :available-output 100    ;make this a function?
               :charging nil ;if its charging this will be a number representing how long its been. 
               }
   :computer {:active true
              :access nil
              :hacked nil
              :scan nil ; a read out of the map, so what the player sees and what the state is are separate.
              ;; if > 2 players then the scan should include which units are friendly and which hostile. 
              :readout nil ;this is a readout of all systems, and may be incorrect if hacked. 
              }
              

   ;; replace this by drawing on the drone definition below. 
   :drones {:deployed nil ;how many are deployed?
            :mode nil ;this will be recon, assault, or defense
            :position nil
            
            }})

(defn assign-weapons-to-bays []
  "Returns a map of what weapons belong to which bays."
  (zipmap (keys (:weapon-bays manowar))
          (split-at 3 (shuffle (keys weapons)))))

(defn assign-system-locations 
  "Returns a map of systems randomly mapped to locations" 
  []
  (zipmap 
   (keys (:system-locations manowar))
   (shuffle  (keys (:locations manowar)))))

;;; generates two starting points
(defn make-starting-points []
  (let [first-point (random-outer-point)
        second-point (opposite-point first-point)]))

;;; This'll assign systems to locations, name your ship, 
(defn make-ship [starting-point]
  (assoc manowar :system-locations (assign-system-locations) 
         :weapon-bays (assign-weapons-to-bays)
         :location starting-point))

;;; Need to figure out path-finding if the course is imprecise. 

