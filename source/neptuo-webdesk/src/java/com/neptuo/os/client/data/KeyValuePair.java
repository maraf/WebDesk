/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.data;

/**
 *
 * @author Mara
 */
public class KeyValuePair<K, V> {

    protected K key;
    protected V value;

    public KeyValuePair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public boolean equals(Object o) {
        if(o == null || !(o instanceof KeyValuePair))
            return false;

        KeyValuePair pair = (KeyValuePair) o;

        return pair.getKey().equals(key) && pair.getValue().equals(value);
    }

    @Override
    public String toString() {
        return "KeyValuePair{" + "key=" + key + "value=" + value + '}';
    }
}
