(ns styx-next.parser
  (:require [om.next :as om]
            [styx-next.utils :refer [guid]]
            [styx-next.utils :refer [ARROW_UP_KEY ARROW_DOWN_KEY]]
            [replumb.core :as replumb]))

(defn add-command-item [st cmd]
  (let [id (count (:command/list st))
        {:keys [success? form value error]} cmd
        command-item {:command/id id
                      :command/success? success?
                      :command/form form
                      :command/value value
                      :command/error (replumb/error->str error)}
        ref [:command/by-id id]]
    (-> st
        (assoc-in ref command-item)
        (update :command/list conj ref)
        (assoc :history/active {:history/index (+ 1 (count (:command/list st)))
                                :history/form nil}))))


(defmulti read om/dispatch)

(defmethod read :app/title
  [{:keys [state]} key _]
  (let [st @state]
    {:value (get st key)}))

(defmethod read :command/list
  [{:keys [state]} key _]
  (let [st @state]
    {:value (into [] (map #(get-in st %)) (get st key))}))

(defmethod read :history/active
  [{:keys [state]} key _]
  {:value (get @state key)})


(defmulti mutate om/dispatch)

(defmethod mutate 'command/add
  [{:keys [state]} _ {:keys [cmd]}]
  {:value  [:command/list]
   :action #(swap! state add-command-item cmd)})


(defn set-active-history-cmd [state new-index cmd]
  (println "set-active-history-cmd" new-index cmd)
  (swap! state assoc :history/active {:history/index new-index
                                      :history/form (str cmd)}))

(defn a [])

(defn navigate-cmd-history [state active-index key-code input-component]
  (condp = key-code
    ARROW_UP_KEY (let [new-index (- active-index 1)
                       cmd (:command/form (get-in @state [:command/by-id new-index]))]
                   (set! (.-value input-component) cmd)
                   (set-active-history-cmd state new-index cmd))
    ARROW_DOWN_KEY (let [new-index (+ active-index 1)
                         cmd (:command/form (get-in @state [:command/by-id new-index]))]
                     (println "KEY DOWN ")
                     (set! (.-value input-component) cmd)
                     (set-active-history-cmd state new-index cmd))
    nil))


(defmethod mutate 'history/navigate
  [{:keys [state]} _ {:keys [key-code input-component]}]
  (let [st @state
        {:keys [history/active]} st]
    {:action (fn [] (navigate-cmd-history state (:history/index active) key-code input-component))}))

(def parser (om/parser {:read read :mutate mutate}))
