(ns insane-noises.sampled-piano
  (:use
   overtone.live
   overtone.inst.sampled-piano))



(sampled-piano (note :c1))


(defn piano-chord
  ([note tonality]
     (doseq [note (chord note tonality)]
       (sampled-piano note)))
  ([note]
     (piano-chord note :major)))

(defn piano-note [a-note]
  (sampled-piano (note a-note)))

#_(piano-note :A4)
#_(do
  (piano-chord :G3)
  (piano-chord :C3)
  (piano-chord :F3)
  (piano-chord :A3))

#_(let [time (now)]
  (at time          (piano-chord :G3))
  (at (+ 1000 time) (piano-chord :C3))
  (at (+ 2000 time) (piano-chord :F3))
  (at (+ 3000 time) (piano-chord :A3)))


(comment
  (with-metro (metronome 100)
    (range 1 2)
    (piano-note :C4))
)

(defmacro with-metro [metro & body]
  `(let [base-beat# (~metro)]
     (loop [[beat-range# expr# & more-beats#] ~body]
        (println "range: " (quote beat-range#))
        (println "expr: " (quote expr#))

        (when-not (nil? more-beats#)
          (recur more-beats#)))))


(comment


  (macroexpand-1
   '(with-metro (metronome 100)
      (range 1)
      (piano-note :F4)

      (range 1 2)
      (piano-note :C4)))


(clojure.core/let [base-beat__21549__auto__ ((metronome 100))]
  (clojure.core/loop [[beat-range__21550__auto__ expr__21551__auto__ & more-beats__21552__auto__] ((range 1) (piano-note :F4) (range 1 2) (piano-note :C4))]

    (clojure.core/println "range: " (quote beat-range__21550__auto__))
    (clojure.core/println "expr: " (quote expr__21551__auto__))
    (clojure.core/when-not (clojure.core/nil? more-beats__21552__auto__)
      (recur more-beats__21552__auto__))))











  )

(defn play-chopsticks []
  (let [metro (metronome 100)]
    (with-metro metro
      (range 6)
      (piano-note :F4)
      (range 6)
      (piano-note :G4)

      (range 6 12)
      (piano-note :E4)
      (range 6 12)
      (piano-note :G4)

      (range 12 18)
      (piano-note :B4)
      (range 12 18)
      (piano-note :D4)

      (range 18 22)
      (piano-note :C5)
      (range 18 22)
      (piano-note :C4)

      (range 22 23)
      (piano-note :D4)
      (range 22 23)
      (piano-note :B4)

      (range 23 24)
      (piano-note :E4)
      (range 23 24)
      (piano-note :A4))))

#_(play-chopsticks)

(comment
  (loop [[x y & more] (range 10)]
    (println (format "x: %s y: %s" x y))
    (if more
      (recur more)))
  )

(defn play-stuff [m beat-num]
  (doseq [beat (range 6)]
    (at (m (+ beat beat-num)) (piano-note :F4))
    (at (m (+ beat beat-num)) (piano-note :G4)))

  (doseq [beat (range 6 12)]
    (at (m (+ beat beat-num)) (piano-note :E4))
    (at (m (+ beat beat-num)) (piano-note :G4)))

  (doseq [beat (range 12 18)]
    (at (m (+ beat beat-num)) (piano-note :B4))
    (at (m (+ beat beat-num)) (piano-note :D4)))

  (doseq [beat (range 18 22)]
    (at (m (+ beat beat-num)) (piano-note :C5))
    (at (m (+ beat beat-num)) (piano-note :C4)))

  (at (m (+ 22 beat-num)) (piano-note :D4))
  (at (m (+ 22 beat-num)) (piano-note :B4))

  (at (m (+ 23 beat-num)) (piano-note :E4))
  (at (m (+ 23 beat-num)) (piano-note :A4)))

#_(def metro (metronome 100))

#_(play-stuff metro (metro))
