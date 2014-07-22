package com.github.caiiiycuk.ast2uml

import com.github.caiiiycuk.ast2uml.clang.ClangAstParser
import com.github.caiiiycuk.ast2uml.render.PlantUml

object Ast2Uml extends App {

  val lines = Iterator.continually(Console.readLine).takeWhile(l => {
    l != null && l.nonEmpty
  }).toList

  implicit val ast = ClangAstParser(lines.toStream)
  println(PlantUml.renderUml)
}