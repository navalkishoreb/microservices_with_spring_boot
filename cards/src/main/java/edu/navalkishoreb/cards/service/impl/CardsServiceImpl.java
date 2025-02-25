package edu.navalkishoreb.cards.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import edu.navalkishoreb.cards.constants.CardsConstants;
import edu.navalkishoreb.cards.dto.CardsDto;
import edu.navalkishoreb.cards.entity.Cards;
import edu.navalkishoreb.cards.exceptions.CardAlreadyExistsException;
import edu.navalkishoreb.cards.exceptions.ResourceNotFoundException;
import edu.navalkishoreb.cards.mapper.CardsMapper;
import edu.navalkishoreb.cards.repository.CardsRepository;
import edu.navalkishoreb.cards.service.ICardsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CardsServiceImpl implements ICardsService {

    private CardsRepository cardsRepository;

    /**
     * @param mobileNumber - Mobile Number of the Customer
     */
    @Override
    public void createCard(String mobileNumber) {
        Optional<Cards> cards = cardsRepository.findByMobileNumber(mobileNumber);
        if (cards.isPresent()) {
            throw new CardAlreadyExistsException("Card already registered with given mobileNumber " + mobileNumber);
        }
        Cards newCard = createNewCard(mobileNumber);
        cardsRepository.save(newCard);
    }

    private Cards createNewCard(String mobileNumber) {
        Cards newCard = new Cards();
        long randomCardNumber = 100000000000L + new Random().nextInt(900000000);
        newCard.setCardNumber(Long.toString(randomCardNumber));
        newCard.setMobileNumber(mobileNumber);
        newCard.setCardType(CardsConstants.CREDIT_CARD);
        newCard.setTotalLimit(CardsConstants.NEW_CARD_LIMIT);
        newCard.setAmountUsed(0);
        newCard.setAvailableAmount(CardsConstants.NEW_CARD_LIMIT);
        return newCard;
    }

    /**
     * @param mobileNumber - Input mobile Number
     * @return Card Details based on a given mobileNumber
     */
    @Override
    public CardsDto fetchCard(String mobileNumber) {
        Cards cards = cardsRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Card", "mobileNumber", mobileNumber));

        return CardsMapper.mapToCardsDto(cards, new CardsDto());
    }

    /**
     * @param cardsDto - CardsDto Object
     * @return boolean indicating if the update of card details is successful or not
     */
    @Override
    public boolean updateCard(CardsDto cardsDto) {
        Cards cards = cardsRepository.findByCardNumber(cardsDto.getCardNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Card", "cardNumber", cardsDto.getCardNumber()));
        Cards updateCards = CardsMapper.mapToCards(cardsDto, cards);
        cardsRepository.save(updateCards);
        return true;
    }

    /**
     * @param mobileNumber - Input Mobile Number
     * @return boolean indicating if the delete of card details is successful or not
     */
    @Override
    public boolean deleteCard(String mobileNumber) {
        Cards cards = cardsRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Card", "mobileNumber", mobileNumber));
        cardsRepository.delete(cards);
        return true;
    }
}
