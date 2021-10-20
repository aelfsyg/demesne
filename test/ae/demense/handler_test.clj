(ns ae.demesne.handler-test
  (:require [ae.demesne.handler :as sut]
            [clojure.test :as t]
            [ae.demesne.event :as event]
            [ae.demesne.item :as item]))

(t/deftest handle-test

  (t/testing "handle-create"

    (t/testing "If provided a non-nil aggregate it will throw an Exception."
      (t/is (thrown? Exception (sut/handle {} (event/create 3 "foo")))))

    (t/testing "Items are properly created with an id and name."
      (let [id 3 name "foo"]
        (t/is (= (sut/handle nil (event/create id name))
                 {::item/id id ::item/name name ::item/active? true})))))

  (t/testing "handle-deactivate"
    (t/testing "An item can be deactivated."
      (let [old-item {::item/active? true}
            event {::event/type :ae.demesne.event.type/deactivate}
            new-item {::item/active? false}]
        (t/is (= new-item (sut/handle old-item event))))))

  (t/testing "handle-check-in"

    (t/testing "One can check-in items."
      (let [old-item {::item/amount 5}
            event {::event/type :ae.demesne.event.type/check-in ::item/amount 3}
            new-item {::item/amount 8}]
        (t/is (= new-item (sut/handle old-item event)))))

    (t/testing "One cannot check-in a negative quantity."
      (t/is (thrown? Exception (sut/handle {} (event/check-in 3 -5))))))

  (t/testing "handle-check-out"
    (t/testing "One cannot check-out a negative quantity."
      (t/is (thrown? Exception (sut/handle {} (event/check-out 3 -5))))))

  (t/testing "handle-rename"
    (t/testing "Items can be renamed."
      (let [old-item {::item/name "foo"}
            name "bar"
            event {::event/type :ae.demesne.event.type/rename ::item/name name}
            new-item {::item/name name}]
        (t/is (= new-item (sut/handle old-item event))))))

  ;;
  )
