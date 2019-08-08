/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem.fx.callback;

/**
 *
 * @author user
 * @param <T>
 */
public interface FileCallBack<T> {
    public void call(T t);
}
