package com.github.caiiiycuk.ast2uml

import org.scalatest._
import com.github.caiiiycuk.ast2uml.clang.ClangAstParser
import com.github.caiiiycuk.ast2uml._
import com.github.caiiiycuk.ast2uml.query.AstQuery
import com.github.caiiiycuk.ast2uml.render.PlantUml

class RenderTest extends FlatSpec with Matchers {

  behavior of "Query"

  it should "render uml classes" in {
    implicit val ast = ClangAstParser(ClangOutput.GEO_LAYER_LIB_CPP)
  }

}