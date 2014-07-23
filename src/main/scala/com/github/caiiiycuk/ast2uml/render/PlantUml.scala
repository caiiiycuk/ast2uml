package com.github.caiiiycuk.ast2uml.render

import com.github.caiiiycuk.ast2uml._
import com.github.caiiiycuk.ast2uml.query.AstQuery

object PlantUml {
  import AstQuery._

  def renderUml()(implicit node: Ast): String = {
    val classes = findAll(classOf[AstRecord])
    val umlClasses = classes.map(renderClass(_)).distinct.sorted
    umlClasses.mkString
  }
  
  def renderClass(path: QueryPath): String = {
    val name = path.map(_ match {
      case AstClass(AstNode(name, _), _) => name
      case AstStruct(AstNode(name, _), _) => name
      case AstNamespace(AstNode(name, _), _) => name
      case _ => ""
    }).filter(!_.isEmpty).mkString("::")
   
    val body = path.last match {
      case astRecord: AstRecord => renderClassBody(astRecord.toClass)
      case _ => ""
    } 
      
    s"""
    |class $name {
    |${body}
    |}
    """.stripMargin
  }
  
  def renderClassBody(node: AstClass): String = {
    val fields = node.childs.flatMap(_ match {
      case field: AstField => {
        field.node.specs.headOption.map {
          case AstAccessSpec("public") => Some(field)
          case _ => None
        }.getOrElse(Some(field))
//        Some(field)
      }
      case _ => None
    }).sortBy(_.astName)
    
    val methods = node.childs.flatMap(_ match {
      case method: AstMethod => {
        method.node.specs.headOption.map {
          case AstAccessSpec("public") => Some(method)
          case _ => None
        }.getOrElse(Some(method))
//        Some(method)
      }
      case _ => None
    }).sortBy(_.astName)
   
    (fields.map(_.astName) ::: methods.map(_.astName)).mkString("\n")
  }
}