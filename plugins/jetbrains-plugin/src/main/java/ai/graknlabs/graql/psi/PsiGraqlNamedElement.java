package ai.graknlabs.graql.psi;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import org.jetbrains.annotations.NotNull;

/**
 * @author <a href="mailto:brandon@srcpl.us">Brandon Fergerson</a>
 */
public abstract class PsiGraqlNamedElement extends PsiGraqlElement implements PsiNameIdentifierOwner {

    public PsiGraqlNamedElement(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public TextRange getTextRange() {
        return getNameIdentifier().getTextRange();
    }

    @Override
    public String getName() {
        PsiElement id = getNameIdentifier();
        return id != null ? id.getText() : null;
    }

    @Override
    public int getTextOffset() {
        PsiElement id = getNameIdentifier();
        return id != null ? id.getTextOffset() : super.getTextOffset();
    }

    @Override
    public String toString() {
        return getNode().getElementType() + " - " + getName() + " - Location: " + getTextRange();
    }
}