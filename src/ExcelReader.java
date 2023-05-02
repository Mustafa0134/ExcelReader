import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

public class ExcelReader {
    public static Map<String, BigDecimal> readExcelFile() {
        Map<String, BigDecimal> sampleData = new TreeMap<>();

        try (InputStream is = ExcelReader.class.getResourceAsStream("/samples.xlsx");
             Workbook workbook = WorkbookFactory.create(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.rowIterator();
            List<Row> rows = new ArrayList<>();
            while (rowIterator.hasNext()) {
                rows.add(rowIterator.next());
            }
            rows.sort(Comparator.comparing(row -> row.getCell(1).toString()));
            for (Row row : rows) {
                String name = row.getCell(1).toString();
                BigDecimal price = BigDecimal.valueOf(row.getCell(2).getNumericCellValue());
                sampleData.put(name, price);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read Excel file: " + e.getMessage(), e);
        }

        return sampleData;
    }
}
