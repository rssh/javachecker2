
package z;

public class ZObject {

    private static native void registerNatives();
    static {
        registerNatives();
    }

    public final native Class<? extends ZObject> getZClass();

    public native int hashCode();

    public boolean equals(Object obj) {
	return (this == obj);
    }

    protected native Object clone() throws CloneNotSupportedException;

    public String toString() {
	return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }

    public final native void znotify();

    public final native void znotifyAll();

    public final native void zwait(long timeout) throws InterruptedException;

    public final void zwait(long timeout, int nanos) throws InterruptedException {
        if (timeout < 0) {
            throw new IllegalArgumentException("timeout value is negative");
        }

        if (nanos < 0 || nanos > 999999) {
            throw new IllegalArgumentException(
				"nanosecond timeout value out of range");
        }

	if (nanos >= 500000 || (nanos != 0 && timeout == 0)) {
	    timeout++;
	}

	wait(timeout);
    }

    public final void zwait() throws InterruptedException {
	wait(0);
    }

    protected void finalize() throws Throwable { }
}
