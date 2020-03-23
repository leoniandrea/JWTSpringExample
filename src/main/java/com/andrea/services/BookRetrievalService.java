package com.andrea.services;

import com.andrea.dto.rest.Book;
import com.andrea.dto.rest.GetBookListResponse;
import com.andrea.session.RequestUserDataContext;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class BookRetrievalService implements IBookRetrievalService {

	protected final RequestUserDataContext requestUserDataContext;

	public BookRetrievalService(RequestUserDataContext requestUserDataContext) {
		this.requestUserDataContext = requestUserDataContext;
	}


	// MOCKED DATA (SERVICE OUT OF SCOPE OF THE PROJECT)

	@Override
	public GetBookListResponse getBookList() {
		if (requestUserDataContext.getAuthenticatedUserName().equals("andrea")) {
			return new GetBookListResponse(Arrays.asList(new Book("La Compagnia dell'Anello")));
		}
		return new GetBookListResponse();
	}
}
