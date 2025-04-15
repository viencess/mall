package com.hmall.item.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hmall.api.dto.ItemDTO;
import com.hmall.common.domain.PageDTO;
import com.hmall.item.domain.po.Item;
import com.hmall.item.domain.query.ItemPageQueryDTO;
import com.hmall.item.service.IItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "搜索相关接口")
@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
@Slf4j
public class SearchController {

    private final IItemService itemService;

    @ApiOperation("商品分页查询")
    @GetMapping("/list")
    public PageDTO<ItemDTO> search(ItemPageQueryDTO query) {
        log.info("商品分页查询:{}", query);
        Page<Item> result = itemService.pageQuery(query);
        return PageDTO.of(result, ItemDTO.class);
    }
}
