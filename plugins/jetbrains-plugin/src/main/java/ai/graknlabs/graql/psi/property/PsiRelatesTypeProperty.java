package ai.graknlabs.graql.psi.property;

import ai.graknlabs.graql.psi.GraqlPsiUtils;
import ai.graknlabs.graql.psi.PsiGraqlNamedElement;
import ai.graknlabs.graql.psi.statement.PsiStatementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author <a href="mailto:bfergerson@apache.org">Brandon Fergerson</a>
 */
public class PsiRelatesTypeProperty extends PsiGraqlNamedElement {

    public PsiRelatesTypeProperty(@NotNull ASTNode node) {
        super(node);
    }

    @Nullable
    @Override
    public PsiElement getNameIdentifier() {
        //todo: seems wrong
        if (getNode().getFirstChildNode() != null
                && getNode().getFirstChildNode().getTreeNext() != null
                && getNode().getFirstChildNode().getTreeNext().getTreeNext() != null) {
            ASTNode idNode = getNode().getFirstChildNode().getTreeNext().getTreeNext();
            if (idNode != null) {
                return idNode.getPsi();
            }
        }
        return null;
    }

    @Override
    public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        return GraqlPsiUtils.setName(this, name);
    }

    @Override
    public String getName() {
        PsiStatementType statementType = (PsiStatementType) getParent();
        return statementType.getName() + ":" + super.getName();
    }
}
