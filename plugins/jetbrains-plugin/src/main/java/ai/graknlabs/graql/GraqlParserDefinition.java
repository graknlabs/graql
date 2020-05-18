package ai.graknlabs.graql;

import ai.graknlabs.graql.psi.PsiGraqlElement;
import ai.graknlabs.graql.psi.PsiGraqlFileBase;
import ai.graknlabs.graql.psi.property.*;
import ai.graknlabs.graql.psi.statement.PsiStatementType;
import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.antlr.intellij.adaptor.lexer.ANTLRLexerAdaptor;
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory;
import org.antlr.intellij.adaptor.lexer.RuleIElementType;
import org.antlr.intellij.adaptor.lexer.TokenIElementType;
import org.antlr.intellij.adaptor.parser.ANTLRParserAdaptor;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.ParseTree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static ai.graknlabs.graql.GraqlLanguage.GRAQL_TYPES;

/**
 * @author <a href="mailto:bfergerson@apache.org">Brandon Fergerson</a>
 */
public class GraqlParserDefinition implements ParserDefinition {

    public static GraqlParserDefinition INSTANCE;

    static {
        PSIElementTypeFactory.defineLanguageIElementTypes(
                GraqlLanguage.INSTANCE, GraqlParser.tokenNames, GraqlParser.ruleNames);
    }

    public static final IFileElementType FILE = new IFileElementType(GraqlLanguage.INSTANCE);
    public static final TokenSet IDS =
            PSIElementTypeFactory.createTokenSet(GraqlLanguage.INSTANCE, GraqlParser.TYPE_NAME_);
    public static final TokenSet COMMENTS =
            PSIElementTypeFactory.createTokenSet(GraqlLanguage.INSTANCE, GraqlLexer.COMMENT);
    public static final TokenSet WHITESPACE =
            PSIElementTypeFactory.createTokenSet(GraqlLanguage.INSTANCE, GraqlLexer.WS);
    public static final TokenSet STRING =
            PSIElementTypeFactory.createTokenSet(GraqlLanguage.INSTANCE, GraqlLexer.STRING_);

    public GraqlParserDefinition() {
        INSTANCE = this;
    }

    @NotNull
    @Override
    public Lexer createLexer(Project project) {
        GraqlLexer lexer = new GraqlLexer(null);
        return new ANTLRLexerAdaptor(GraqlLanguage.INSTANCE, lexer);
    }

    @NotNull
    public TokenSet getWhitespaceTokens() {
        return WHITESPACE;
    }

    @NotNull
    public TokenSet getCommentTokens() {
        return COMMENTS;
    }

    @NotNull
    public TokenSet getStringLiteralElements() {
        return STRING;
    }

    @NotNull
    public PsiParser createParser(final Project project) {
        return new ANTLRParserAdaptor(GraqlLanguage.INSTANCE, new GraqlParser(null)) {
            @Override
            protected ParseTree parse(Parser parser, IElementType root) {
                if (root instanceof IFileElementType) {
                    return ((GraqlParser) parser).eof_query_list();
                }
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public IFileElementType getFileNodeType() {
        return FILE;
    }

    public PsiFile createFile(FileViewProvider viewProvider) {
        return new PsiGraqlFileBase(viewProvider);
    }

    @NotNull
    public PsiElement createElement(ASTNode node) {
        IElementType elType = node.getElementType();
        if (elType instanceof TokenIElementType) {
            return new PsiGraqlElement(node);
        }
        if (!(elType instanceof RuleIElementType)) {
            return new PsiGraqlElement(node);
        }

        RuleIElementType ruleElType = (RuleIElementType) elType;
        switch (ruleElType.getRuleIndex()) {
            case GraqlParser.RULE_statement_type:
                return new PsiStatementType(node);
            case GraqlParser.RULE_type_property:
                PsiElement ruleTypePropertyElement = getRuleTypePropertyElement(node);
                if (ruleTypePropertyElement != null) return ruleTypePropertyElement;
            case GraqlParser.RULE_type:
                PsiElement ruleTypeElement = getRuleTypeElement(node);
                if (ruleTypeElement != null) return ruleTypeElement;
            default:
                return new PsiGraqlElement(node);
        }
    }

    @Nullable
    public static PsiElement getRuleTypeElement(ASTNode node) {
        if (node.getTreePrev() != null && node.getTreePrev().getTreePrev() != null
                && node.getTreePrev().getTreePrev().getText().equals("as")) {
            return new PsiRelatesSuperRoleTypeProperty(node);
        } else if (node.getTreeNext() != null && node.getTreeNext().getTreeNext() != null
                && node.getTreeNext().getTreeNext().getFirstChildNode() != null
                && node.getTreeNext().getTreeNext().getFirstChildNode().getText().equals("sub")) {
            return new PsiTypeProperty(node);
        }
        return null;
    }

    @Nullable
    public static PsiElement getRuleTypePropertyElement(ASTNode node) {
        if (node.getFirstChildNode() != null && (node.getFirstChildNode().getText().equals("has")
                || node.getFirstChildNode().getText().equals("key"))) {
            String hasTo = node.getLastChildNode().getText();
            if (!hasTo.isEmpty()) return new PsiHasTypeProperty(node);
        } else if (node.getFirstChildNode() != null && node.getFirstChildNode().getText().equals("plays")) {
            String playsTo = node.getLastChildNode().getText();
            if (!playsTo.isEmpty()) return new PsiPlaysTypeProperty(node);
        } else if (node.getFirstChildNode() != null && node.getFirstChildNode().getText().equals("relates")) {
            String relatesTo = node.getLastChildNode().getText();
            if (!relatesTo.isEmpty()) return new PsiRelatesTypeProperty(node);
        } else if (node.getFirstChildNode() != null && node.getFirstChildNode().getText().equals("sub")) {
            String subsTo = node.getLastChildNode().getText();
            if (!subsTo.isEmpty() && !GRAQL_TYPES.contains(subsTo)) return new PsiSubTypeProperty(node);
        }
        return null;
    }
}