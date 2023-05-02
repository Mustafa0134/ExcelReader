import javax.swing.*;
import java.math.BigDecimal;
import java.util.Map;

public class ApplicationRunner {
    public static void main(String[] args) {
        // load sample data from Excel file using ExcelReader

        Map<String, BigDecimal> sampleData = ExcelReader.readExcelFile();

        // create and show the Main window
        SwingUtilities.invokeLater(() -> {
            Main main = new Main(sampleData);
            main.pack();
            main.setLocationRelativeTo(null);
            main.setVisible(true);
        });
    }
}


