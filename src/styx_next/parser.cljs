(ns styx-next.parser
  (:require [om.next :as om]
            [styx-next.utils :refer [guid]]
            [replumb.core :as replumb]))

(defn add-command-item [st cmd]
  (let [id (guid)
        {:keys [success? form value error]} cmd
        command-item {:command/id id
                      :command/success? success?
                      :command/form form
                      :command/value value
                      :command/error (replumb/error->str error)}
        ref [:command/by-id id]]
    (-> st
        (assoc-in ref command-item)
        (update :command/list conj ref))))


(defmulti read om/dispatch)

(defmethod read :app/title
  [{:keys [state]} key _]
  (let [st @state]
    {:value (get st key)}))

(defmethod read :command/list
  [{:keys [state]} key _]
  (let [st @state]
    {:value (into [] (map #(get-in st %)) (get st key))}))


(defmulti mutate om/dispatch)

(defmethod mutate 'command/add
  [{:keys [state]} _ {:keys [cmd]}]
  ;;(println @state)
  {:value  [:command/list]
   :action #(swap! state add-command-item cmd)})


(def parser (om/parser {:read read :mutate mutate}))
