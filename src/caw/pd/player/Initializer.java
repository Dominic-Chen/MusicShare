/**
 * 
 */
package caw.pd.player;


/**
 * @author domy
 *
 */
public interface Initializer<T> {
	public void initialize(T object, Callback callback);
}
