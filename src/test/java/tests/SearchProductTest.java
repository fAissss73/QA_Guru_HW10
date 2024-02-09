package tests;


import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;


public class SearchProductTest extends TestBase {
    @BeforeEach
    void setUp() {
        open("https://www.avito.ru/");
    }

    @ValueSource(strings = {
            "IPhone 15","IPhone 14"
    }
    )

    @ParameterizedTest(name = "Результат поиска {0} в поисковой строке")
    void searchResultShouldNotBeEmpty (String SearchPhone) {
        $("[data-marker='search-form/suggest']").setValue(SearchPhone).pressEnter();
        $$("[data-marker='catalog-serp'] [data-marker='item']")
        .shouldBe(sizeGreaterThan(0));
    }

    @CsvSource(value = {
            "Audi , Audi",
            "Mercedes-Benz , Mercedes-Benz"
    })
    @ParameterizedTest(name = "При результате поиска {0} в поле марка присутствует категория{1}")
    void searchResultShouldBeHaveMark (String SearchCar, String CategoryResult) {
        $("[data-marker='search-form/suggest']").setValue(SearchCar).pressEnter();
        $("[data-marker='params[110000]/multiselect']").shouldHave(Condition.text(CategoryResult));
    }


    static Stream<Arguments> searchResultShouldBeHaveSomeItems() {
        return Stream.of(
                Arguments.of(
                        ("Audi"),
                        List.of("100", "80", "A1", "A3", "A4", "A5", "A6", "A6 Allroad Quattro", "A7", "A8", "e-tron", "Q3", "Q5", "Q7")
                ),
                Arguments.of(
                        ("Mercedes-Benz"),
                        List.of("190 (W201)", "A-класс", "CLA-класс", "CLS-класс", "CL-класс", "C-класс", "E-класс", "GLC-класс", "GLE-класс", "GLE-класс Coupe", "GLK-класс", "GLS-класс", "GL-класс", "G-класс", "M-класс", "R-класс", "S-класс", "S-класс AMG", "T1", "Vaneo", "Vito", "V-класс", "W123", "W124", "W136")

                )
        );
    }

    @MethodSource()
    @ParameterizedTest(name = "При рехультате поиска {0} в присутствует список моделей {1}")
    void searchResultShouldBeHaveSomeItems (String SearchCar, List<String> expectedModel) {
        $("[data-marker='search-form/suggest']").setValue(SearchCar).pressEnter();
        $$("[data-marker='params[110001]/multiselect-outline-input'] a").filter(visible)
                .shouldHave(texts(expectedModel));
    }
}
