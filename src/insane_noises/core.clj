(ns insane-noises.core
  (:use
   overtone.live))

(definst hat [volume 1.0]
  (let [src (white-noise)
        env (env-gen (perc 0.001 0.3) :action FREE)]
    (* volume 1 src env)))

(def kick (sample (freesound-path 2086)))


(def *times* 0)
(defn loop-beats [time]
  (at (+    0 time) (kick))
  (at (+  400 time) (hat))
  (at (+  800 time) (kick))
  (at (+ 1200 time) (hat))
  (def *times* (inc *times*))
  (if (< *times* 4)
    (apply-at (+ 1600 time) loop-beats (+ 1600 time) [])))


(defonce metro (metronome 240))


(defn weak-hat []
  (hat 0.3))

(defn phat-beats [m beat-num]
  (at (m (+ 0 beat-num)) (kick) (weak-hat))
  (at (m (+ 1 beat-num)) (kick))
  (at (m (+ 2 beat-num))        (hat))
  (at (m (+ 3 beat-num)) (kick) (weak-hat))
  (at (m (+ 4 beat-num)) (kick) (weak-hat))
  (at (m (+ 4.5 beat-num)) (kick))
  (at (m (+ 5 beat-num)) (kick))
  (at (m (+ 6 beat-num)) (kick) (hat))
  (at (m (+ 7 beat-num))        (weak-hat))
  #_(apply-at (m (+ 8 beat-num)) phat-beats m (+ 8 beat-num) []))

(definst dubstep [freq 100 wobble-freq 5]
  (let [sweep (lin-exp (lf-saw wobble-freq) -1 1 40 5000)
        son   (mix (saw (* freq [0.99 1 1.01])))]
    (lpf son sweep)))

(comment

  (dubstep)
  (phat-beats metro (metro))
  )


(definst square-wave [freq 440] (square freq))

(def times (take 220 (iterate #(+ 30 %) 0)))

(defn change-pitch [t f inst] (at (+ t (now)) (ctl inst :freq f)))

(defn falling-pitches [start] (take (/ start 2) (iterate dec start)))
(defn rising-pitches [start] (take start (iterate inc start)))

(defn slide [pitches inst] (map (fn [x y] (change-pitch x y inst)) times pitches))

(comment

  (square-wave)


  (slide (falling-pitches 440) square-wave)

  (slide (rising-pitches 220) square-wave)

  (stop)

  )


(definst saw-wave [freq 440 attack 0.01 sustain 0.4 release 0.1 vol 0.4]
  (* (env-gen (lin-env attack sustain release) 1 1 0 1 FREE)
     (saw freq)
     vol))

(saw-wave 440)
(saw-wave 523.25)
(saw-wave 261.62) ; This is C4

(saw-wave (midi->hz (note :A4)))
(saw-wave (midi->hz (note :C5)))
(saw-wave (midi->hz (note :C4))) ; This is C4! Surprised?

(defn saw2 [music-note]
  (saw-wave (midi->hz (note music-note))))

(defn play-chord [a-chord]
  (doseq [note a-chord] (saw2 note)))

(play-chord (chord :A4 :minor))

(defn chord-progression-time []
  (let [time (now)]
    (at time (play-chord (chord :C4 :major)))
    (at (+ 2000 time) (play-chord (chord :G3 :major)))
    (at (+ 3000 time) (play-chord (chord :F3 :sus4)))
    (at (+ 4300 time) (play-chord (chord :F3 :major)))
    (at (+ 5000 time) (play-chord (chord :G3 :major)))))

(chord-progression-time)

;; or beats:
(defonce metro (metronome 120))
(metro)
(defn chord-progression-beat [m beat-num]
  (at (m (+ 0 beat-num)) (play-chord (chord :C4 :major)))
  (at (m (+ 4 beat-num)) (play-chord (chord :G3 :major)))
  (at (m (+ 8 beat-num)) (play-chord (chord :A3 :minor)))
  (at (m (+ 14 beat-num)) (play-chord (chord :F3 :major))))

(chord-progression-beat metro (metro))

(defn chord-progression-beat [m beat-num]
  (at (m (+ 0 beat-num)) (play-chord (chord :C4 :major)))
  (at (m (+ 2 beat-num)) (play-chord (chord :C4 :major)))
  (at (m (+ 2 beat-num)) (play-chord (chord :G3 :major)))
  (at (m (+ 8 beat-num)) (play-chord (chord :A3 :minor)))
  (at (m (+ 12 beat-num)) (play-chord (chord :F3 :major)))
  (apply-at (m (+ 16 beat-num)) chord-progression-beat m (+ 16 beat-num) []))
(chord-progression-beat metro (metro))

(stop)
