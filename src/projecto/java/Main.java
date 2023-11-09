package projecto.java;

import views.Window;
import views.auth.Login;

public class Main {

    public static void main(String[] args) {
        Window win = new Window();
        win.setTitle("Dream Matches");
        // chamar login
        win.render(new Login(), "");
        win.setLocationRelativeTo(null);
        win.setVisible(true);
          
    }
}