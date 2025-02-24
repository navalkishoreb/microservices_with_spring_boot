package edu.navalkishoreb.cards.service.impl;

import edu.navalkishoreb.cards.dto.CardsDto;
import edu.navalkishoreb.cards.service.ICardsService;
import org.springframework.stereotype.Service;

@Service
public class CardsServiceImpl implements ICardsService {
    /**
     * @param mobileNumber - Mobile Number of the Customer
     */
    @Override
    public void createCard(String mobileNumber) {

    }

    /**
     * @param mobileNumber - Input mobile Number
     * @return Card Details based on a given mobileNumber
     */
    @Override
    public CardsDto fetchCard(String mobileNumber) {
        return CardsDto.builder().build();
    }

    /**
     * @param cardsDto - CardsDto Object
     * @return boolean indicating if the update of card details is successful or not
     */
    @Override
    public boolean updateCard(CardsDto cardsDto) {
        return false;
    }

    /**
     * @param mobileNumber - Input Mobile Number
     * @return boolean indicating if the delete of card details is successful or not
     */
    @Override
    public boolean deleteCard(String mobileNumber) {
        return false;
    }
}
