/*
 * Sonar PL/SQL Plugin (Community)
 * Copyright (C) 2015-2018 Felipe Zorzo
 * mailto:felipebzorzo AT gmail DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.plsqlopen.checks;

import java.util.List;

import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.plsqlopen.api.PlSqlGrammar;
import org.sonar.plsqlopen.annnotations.ActivatedByDefault;
import org.sonar.plsqlopen.annnotations.ConstantRemediation;

import com.sonar.sslr.api.AstNode;

@Rule(
    key = EmptyBlockCheck.CHECK_KEY,
    priority = Priority.MINOR,
    tags = Tags.UNUSED
)
@ConstantRemediation("5min")
@ActivatedByDefault
public class EmptyBlockCheck extends AbstractBaseCheck {
    public static final String CHECK_KEY = "EmptyBlock";

    @Override
    public void init() {
        subscribeTo(PlSqlGrammar.STATEMENTS_SECTION);
    }

    @Override
    public void visitNode(AstNode suiteNode) {
        List<AstNode> statements = suiteNode.getFirstChild(PlSqlGrammar.STATEMENTS).getChildren(PlSqlGrammar.STATEMENT);
        if (statements.size() == 1) {
            List<AstNode> nullStatementSelect = statements.get(0).getChildren(PlSqlGrammar.NULL_STATEMENT);
            if (!nullStatementSelect.isEmpty()) {
                addLineIssue(getLocalizedMessage(CHECK_KEY), statements.get(0).getTokenLine());
            }
        }
    }
}
