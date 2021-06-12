package hello.itemservice.domain.item;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;


public class ItemRepositoryTest {

    ItemRepository itemRepository = new ItemRepository();

    @AfterEach
    void afterEach(){
        itemRepository.clearStore();
    }


    @Test
    @DisplayName("Item 저장 테스트")
    public void save(){
        //given
        Item item = new Item("itemName", 10000, 10);

        //when
        Item saveItem = itemRepository.save(item);

        //then
        Item findItem = itemRepository.findById(saveItem.getId());

        assertThat(saveItem).isEqualTo(findItem);

    }

    @Test
    @DisplayName("Item 전체 조회 테스트")
    public void findAll(){
        //given
        Item item1 = new Item("itemName", 10000, 10);
        Item item2 = new Item("itemName", 20000, 10);

        //when
        itemRepository.save(item1);
        itemRepository.save(item2);
        List<Item> result = itemRepository.findAll();

        //then
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Item 수정 테스트 ")
    public void updateItem(){

        //given
        Item item1 = new Item("itemName", 10000, 10);

        //when
        itemRepository.save(item1);
        Item findItem = itemRepository.findById(item1.getId());
        findItem.setItemName("itemName1");

        assertThat(item1).isNotSameAs(findItem);


        //then
    }
}