package lab_6_IS;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;

//Class to handle global mouse events

public class NativeMouseListenerEventsHandler implements NativeMouseInputListener {
	
	private boolean isDragged = false, isReleased = false;
	
	public NativeMouseListenerEventsHandler(){
		LogManager.getLogManager().reset();
		Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.OFF);
	}

	@Override
	public void nativeMouseDragged(NativeMouseEvent arg0) {
		isDragged = true;
	}

	@Override
	public void nativeMouseMoved(NativeMouseEvent arg0) {
	}

	@Override
	public void nativeMouseClicked(NativeMouseEvent arg0) {
	}

	@Override
	public void nativeMousePressed(NativeMouseEvent arg0) {
	}

	@Override
	public void nativeMouseReleased(NativeMouseEvent arg0) {
		isReleased = true;
		if(isReleased == true && Main.isExited() == true && Main.getTable().getSelectedRows() != null && isDragged == true){
			int[] rows = Main.getTable().getSelectedRows();
			ExtendedTableModel model = (ExtendedTableModel) Main.getTable().getModel();
			for(int i : rows){
				model.removeRow(i);
			}
		}
		isDragged = false;
		Main.setExited(false);
	}

}
