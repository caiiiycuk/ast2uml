package com.github.caiiiycuk.ast2uml.query

import com.github.caiiiycuk.ast2uml._

object AstQuery {
  type QueryPath = List[Ast]

  def findAll[T](prototype: Class[T])(implicit ast: Ast): List[QueryPath] =
    findAll(prototype, ast, List())
    
  private def findAll[T](prototype: Class[T], astPart: Ast, path: List[Ast]): List[QueryPath] = {
    val newPath = path :+ astPart
    val paths = astPart.astChilds.flatMap(findAll(prototype, _, newPath)).toList
    
    if (prototype.isAssignableFrom(astPart.getClass())) {
      List(newPath) ::: paths
    } else {
      paths
    }
  }

}