package core;

public interface UIAction {
	public default void onClick(UIMenu menu) {

	}

	public default void onClick(UIMenuItem item) {

	}

	public default void onClick(UIMenu menu, UIMenuItem item) {

	}
}
