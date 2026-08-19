package com.financialtransaction.financialtransaction;

import com.financialtransaction.dto.TransactionRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
class FinancialtransactionApplicationTests {

		@Test
		void shouldGenerate1000Transactions(){

			List<TransactionRequestDTO> list = new ArrayList<>();

			for(int i=0;i<1000;i++){

				TransactionRequestDTO dto = new TransactionRequestDTO();

				dto.setSourceAccountId(1L);
				dto.setTargetAccountId(2L);
				dto.setAmount(BigDecimal.valueOf(10));
				dto.setTimestamp(LocalDateTime.now());

				list.add(dto);
			}

			assertEquals(1000,list.size());

		}

		@Test
		void shouldGenerate5000Transactions(){

			List<TransactionRequestDTO> list = new ArrayList<>();

			for(int i=0;i<5000;i++){

				TransactionRequestDTO dto = new TransactionRequestDTO();

				dto.setSourceAccountId(2L);
				dto.setTargetAccountId(3L);
				dto.setAmount(BigDecimal.valueOf(5));
				dto.setTimestamp(LocalDateTime.now());

				list.add(dto);
			}

			assertEquals(5000,list.size());

		}

}
