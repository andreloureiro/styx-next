(ns styx-next.reconciler
  (:require [om.next :as om]
            [styx-next.parser :refer [parser]]
            [styx-next.state :refer [initial-state]]))

(defonce reconciler (om/reconciler {:state initial-state :parser parser}))
