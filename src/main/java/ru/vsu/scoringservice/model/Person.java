package ru.vsu.scoringservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Сущность клиента")
public class Person {
    @Schema(description = """
            Состояние существующего расчетного счета \n
            A11 : ... < 0 DM
            A12 : 0 <= ... < 200 DM
            A13 : ... >= 200 DM / salary assignments for at least 1 year
            A14 : no checking account""")
    private String checkingStatus;
    @Schema(description = "Длительность кредита")
    private Integer duration;
    @Schema(description = """
            Кредитная история
            A30 : no credits taken/ all credits paid back duly
            A31 : all credits at this bank paid back duly
            A32 : existing credits paid back duly till now
            A33 : delay in paying off in the past
            A34 : critical account/ other credits existing (not at this bank)
            """)
    private String creditHistory;
    @Schema(description = """
            Цель кредита
            A40 : car (new)
            A41 : car (used)
            A42 : furniture/equipment
            A43 : radio/television
            A44 : domestic appliances
            A45 : repairs
            A46 : education
            A47 : (vacation - does not exist?)
            A48 : retraining
            A49 : business
            A410 : others""")
    private String purpose;
    @Schema(description = """
            Размер кредита""")
    private Integer creditAmount;
    @Schema(description = """
            Сберегательный счет/облигации
            A61 : ... < 100 DM
            A62 : 100 <= ... < 500 DM
            A63 : 500 <= ... < 1000 DM
            A64 : .. >= 1000 DM
            A65 : unknown/ no savings account""")
    private String savingStatus;
    @Schema(description = """
            Стаж работы
            A71 : unemployed
            A72 : ... < 1 year
            A73 : 1 <= ... < 4 years
            A74 : 4 <= ... < 7 years
            A75 : .. >= 7 years""")
    private String employment;
    @Schema(description = "Ставка рассрочки в процентах от располагаемого дохода")
    private Integer installmentCommitment;
    @Schema(description = """
            Социальный статус и пол
            A91 : male : divorced/separated
            A92 : female : divorced/separated/married
            A93 : male : single
            A94 : male : married/widowed
            A95 : female : single
            """)
    private String personalStatus;
    @Schema(description = """
            Прочие должники/поручители
            A101 : none
            A102 : co-applicant
            A103 : guarantor""")
    private String otherParties;
    @Schema(description = "Длительность настоящего места жительства")
    private Integer residenceSince;
    @Schema(description = """
            Собственность
            A121 : real estate
            A122 : if not A121 : building society savings agreement/ life insurance
            A123 : if not A121/A122 : car or other, not in attribute 6
            A124 : unknown / no property""")
    private String propertyMagnitude;
    @Schema(description = "Возраст")
    private Integer age;
    @Schema(description = """
            Другие планы рассрочки
            A141 : bank
            A142 : stores
            A143 : none""")
    private String otherPaymentPlans;
    @Schema(description = """
            Жильё
            A151 : rent
            A152 : own
            A153 : for free""")
    private String housing;
    @Schema(description = "Количество существующих кредитов в этом банке")
    private Integer existingCredits;
    @Schema(description = """
            Работа
            A171 : unemployed/ unskilled - non-resident
            A172 : unskilled - resident
            A173 : skilled employee / official
            A174 : management/ self-employed/
            highly qualified employee/ officer""")
    private String job;
    @Schema(description = "Количество лиц, по которым возложена обязанность по содержанию")
    private String numDependents;
    @Schema(description = """
            Телефон
            A191 : none
            A192 : yes, registered under the customers name""")
    private String ownTelephone;
    @Schema(description = """
            Иностранный работник
            A201 : yes
            A202 : no""")
    private String foreignWorker;
}
