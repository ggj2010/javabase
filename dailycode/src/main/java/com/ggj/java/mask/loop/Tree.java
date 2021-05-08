package com.ggj.java.mask.loop;

import lombok.Data;

import java.util.List;

/**
 * @author gaoguangjin
 */
@Data
public class Tree {
  private int id;
  private int parentId;
  private String name;
  private List<Tree> child;

  public Tree(int id, int parentId, String name) {
    this.id = id;
    this.parentId = parentId;
    this.name = name;
  }
}
