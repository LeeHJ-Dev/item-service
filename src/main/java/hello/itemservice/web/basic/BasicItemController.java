package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Controller
@RequestMapping(value = "/basic/items")
//@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository; // git test !!!! 2차 3차 4차 5차

    /**
     * @Autowired : BasicItemController Component 생성 및 BasicItemController 생성자 호출 시 자동으로 Autowired
     *  ex. this.itemRepository = (new ItemRepository() 으로 생성된 주소값(참조값)을 주입한다. 디펜더시)
     * @RequiredArgConstructor 애노테이션은 클래스에 선언된 프로퍼티를 자동으로 할당해주는 생성자를 생성한다.
     * @param itemRepository
     */
    @Autowired
    public BasicItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    /**
     * @PostConstruct : BasicItemController Class Servlet Bean 으로 등록 후 생성자 생성 후 아래의 init() 함수를 호출한다.
     */
    @PostConstruct
    public void init(){
        itemRepository.save(new Item("ItemA", 10000, 10));
        itemRepository.save(new Item("ItemB", 20000, 20));
        itemRepository.save(new Item("ItemC", 30000, 30));
    }

    /**
     * 웹뷰에서 아래의 Path 유입 시 itemRepository select.
     * @GetMapping : @RequestMapping(value = "/basic/items", method = RequestMethod.GET) 동일하다.
     * Path : http://localhost:8080/basic/items
     * @param model
     * @return 웹뷰 랜더링 경로 (http://localhost:8080/basic/items)
     */
    @GetMapping
    public String items(Model model){
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    /**
     * 웹뷰에서 아래의 Path(itemId = 1) 유입 시 itemRepository select.
     * @GetMapping(value = "/{itemId}") -> @GetMapping(value = "/basic/items/1") -> @RequestMapping(value="/basic/item/1", method=RequestMethod.GET)
     * Path : localhost:8080/basic/items/1
     * Value : th:href="@{/basic/items/{itemId}(itemId=${item.id})}" => Long itemId
     * @param itemId itemId PathVariable -> ArgumentResolver -> Parameter Bind
     * @param model 웹뷰 랜더링 시 참고하는 Model Class. item 조회 후 (key, value) Bind.
     * @return 웹뷰 랜더링 경로 (http://localhost:8080/basic/item)
     */
    @GetMapping(value="/{itemId}")
    public String item(@PathVariable("itemId") Long itemId, Model model){
        System.out.println("BasicItemController.item");

        Item findItem = itemRepository.findById(itemId);
        model.addAttribute("item",findItem);
        return "basic/item";
    }

    //th:onclick="|location.href='@{/basic/items/{itemId}/edit(itemId=${item.id})}'|"

    /**
     * 웹뷰에서 아래의 Path(itemId = 1) 선택 후 editForm itemRepository return
     * @GetMapping(value="/{item}/edit) -> @GetMapping(value="/basic/items/1/edit")->@RequestMapping(value="/basic/item/1/edit",method=RequestMethod.GET)
     * Path : Http://localhost:8080/basic/items/1/edit
     * @param itemId itemId PathVariable -> ArgumentResolver -> Parameter Bind
     * @param model 웹뷰 랜더링 시 editItem 항목 저장 리턴.
     * @return 웹뷰 랜더링 경로 (http://localhost:8080/basic/editForm)
     */
    @GetMapping(value="/{itemId}/edit")
    public String itemUpdate(@PathVariable("itemId") Long itemId, Model model){
        System.out.println("BasicItemController.itemUpdate");

        Item findItem = itemRepository.findById(itemId);
        model.addAttribute("item",findItem);
        return "basic/editForm";
    }

    @PostMapping(value="/{itemId}/edit")
    public String edit(@PathVariable("itemId") Long itemId, @ModelAttribute Item item){
        System.out.println("BasicItemController.edit");

        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}";
    }



    /**
     * 웹뷰에서 아래의 Path 선택 후 reDirectPath 제공.
     * @return 웹뷰 랜더링 경로 (Http://localhost:8080/basic/addForm)
     */
    @GetMapping(value="/addForm")
    public String addForm(){
        System.out.println("BasicItemController.addForm");
        return "/basic/addForm";
    }



    @PostMapping(value="/addForm")
    public String addItem(@ModelAttribute Item item){
        System.out.println("BasicItemController.addItem");
        Item findItem = itemRepository.save(item);
        return "/basic/item";
    }

}
