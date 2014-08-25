(ns manowar.ships
  (:require [manowar.map :refer :all]))

(def location-health 25)
(def charge-drain 5)
(def engine-drain 5)
(def course-correction-drain 50)        ;this makes changing course quite expensive

;;; a weapon will have hull and shield damage, a cost, range, range-penalty/miss-chance, and speed. Some will have course/destination, charging?, and a firing mode amongst other specific parameters. 
;;; WATCH OUT, so some things will have charge, and I'll need to know how long it's been charging, so
;;; the ship may need to contain its own copy of this info. 
  ;; (weapons '((nuke (hull-dmg 50) (shield-dmg 30) (power 25) (range 2) (range-penalty -10))
  ;;            (missiles (hull-dmg 4) (shield-dmg 1) (power 20) (range 2) (range-penalty -15))
  ;;            (gauss (target-dmg 20) (hull-dmg 10) (power 20) (range 3) (range-penalty -20)) 
  ;;            ;particle beam will have additional power drain for each dmg point increase, and does extra damage. When its fired (damage-inc) is 
  ;;            ;added, and that adds to the 2 * inc to the power cost. Also needs to be changed so it is maintained, instead of a one-shot!
  ;;            ;problem, damage-inc will damage the hull also, even the hull-dmg is 0. 
  ;;            (particle-beam (hull-dmg 0) (shield-dmg 30) (power 10) (range 10) (range-penalty 0)) 
  ;;            ;will have more drain the longer charged and do more damage. charge adds 5 * charge to dmg, and 2 * charge to power. 
  ;;            ;need to set it up so this doesn't fire automatically, but charges, and then you choose to fire it. Have ion cannon
  ;;            ;do damage to max-shield rather than do some power drain.
  ;;            (ion-cannon (hull-dmg 0) (shield-dmg 15) (power-damage 15) (power 5) (range 10) (range-penalty -15)) ;damage power.max-power - 5 * charge
  ;;            (cannons (hull-dmg 15) (shield-dmg 5) (power 5) (range 2) (range-penalty -20)))) 
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

;;; not sure this is the right way to use this
(defn available-output [ship]
  (- (:max-output (:generator ship)) (:current-output (:generator ship))))

(def manowar  
  {:name nil 
   :captain nil 
   :hull 100
   :shield 0
   :power 100                           ;this is calculated by (- max drain) in :generator. moved to engine
   :engine {:speed-current 0
            :speed-setting 0
            :max-speed 4
            :acceleration 2}  ; add the cost per unit of speed
   ;; put these in :navigation, or in computer
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
               :available-output 100    ;make this a function? (- max current)
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
          (map #(into [] %) (split-at 3 (shuffle (keys weapons))))))

(defn assign-system-locations 
  "Returns a map of systems randomly mapped to locations" 
  []
  (zipmap 
   (keys (:system-locations manowar))
   (shuffle  (keys (:locations manowar)))))

;;; generates two starting points, one opposite the other. 
(defn make-starting-points []
  "Creates a starting point and a second one opposite it."
  (let [first-point (random-outer-point)
        second-point (opposite-point first-point)]
    [first-point second-point]))

;;; This'll assign systems to locations, name your ship, 
;;; should ships start with a destination and speed? It'd make more sense, not like they're just sittin there
;;; but then the cost to change it might be too high!
(defn make-ship [starting-point]
  (assoc manowar :system-locations (assign-system-locations) 
         :weapon-bays (assign-weapons-to-bays)
         :location starting-point
         :engine (assoc (:engine manowar) :speed-setting 1 :speed-current 1)
;         :name name
 ;        :captain captain
         ))
;;; randomize ship name from a database of historical ship names. 

(defn make-ship-pair []
  "Returns a map of two ships starting at opposite points on the outermost ring."
  (let [starting-points (make-starting-points)
        point1 (first starting-points)
        point2 (second starting-points)]
    {1  (make-ship point1) 2 (make-ship point2)}))


