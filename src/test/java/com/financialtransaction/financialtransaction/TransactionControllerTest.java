package com.financialtransaction.financialtransaction;

import com.financialtransaction.controller.TransactionController;
import com.financialtransaction.entity.Transaction;
import com.financialtransaction.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransactionService transactionService;

    @Test
    void getTransactionsGreaterThanThousandTest() throws Exception{
        Transaction transaction = Transaction.builder()
                .id(1L)
                .sourceAccountId(1001L)
                .targetAccountId(2001L)
                .amount(new BigDecimal("1500.00"))
                .build();

        Transaction transaction2 = Transaction.builder()
                .id(2L)
                .sourceAccountId(1002L)
                .targetAccountId(2002L)
                .amount(new BigDecimal("2500.00"))
                .build();

        List<Transaction> transactions = List.of(transaction, transaction2);

        when(transactionService.getTransactionGreaterThanThousand())
                .thenReturn(transactions);

        mockMvc.perform(
                get("/api/v1/transactions/transactionGreaterThanThousand")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].amount").value(1500.00))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].amount").value(2500.00));

        verify(transactionService, times(1))
                .getTransactionGreaterThanThousand();
    }

    //@Test
    void getTransactionsGreaterThanThousand_whenNoTransactions()
            throws Exception {

        when(transactionService.getTransactionGreaterThanThousand())
                .thenReturn(List.of());

        mockMvc.perform(
                        get("/api/v1/transactions/transactionGreaterThanThousand")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(transactionService, times(1))
                .getTransactionGreaterThanThousand();
    }

}
