package com.github.caiiiycuk.ast2uml

abstract class Ast(name: String, childs: Seq[Ast]) {

  def astChilds = childs

  def astName = name

  protected def toString(paddingChar: Char = '~', padding: String = ""): String = {
    val childString = childs.map(_.toString(paddingChar, padding + paddingChar)).mkString
    val astClass = s"${padding}(${getClass.getSimpleName})"
    val postfix = " " * (20 - astClass.length)
    s"${astClass}${postfix}${astName}\n${childString}"
  }

  override def toString() = toString('~')
}

case class AstRoot(childs: List[Ast]) extends Ast(".", childs)

case class AstNamespace(name: String, childs: List[Ast]) extends Ast(name, childs)

abstract class AstRecord(name: String, childs: List[Ast]) extends Ast(name, childs) {
  def toStruct = AstClass(name, childs)
  def toClass = AstClass(name, childs)
}

case class AstClass(name: String, childs: List[Ast]) extends AstRecord(name, childs)
case class AstStruct(name: String, childs: List[Ast]) extends AstRecord(name, childs)
case class AstUnknown(name: String, childs: List[Ast]) extends Ast(name, childs)

case class AstField(name: String, `type`: Option[String], childs: List[Ast]) extends Ast(name, childs) {
  override def astName = s"${`type`.getOrElse("???")} ${name}"
}

case class AstMethod(name: String, signature: Option[String], childs: List[Ast]) extends Ast(name, childs) {
  override def astName = s"${name}(${signature.getOrElse("???")})"
}