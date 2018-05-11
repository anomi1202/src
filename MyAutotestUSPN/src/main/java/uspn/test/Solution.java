package uspn.test;

import uspn.test.appmanager.ApplicationManager;

import java.io.IOException;

import static uspn.test.appmanager.data.ServerType.*;

public class Solution {

    public static void main(String[] args) throws IOException {
        ApplicationManager app = new ApplicationManager();
        app.initApp();
        app.loginTo(DEV);
        app.exit();
    }
}
