/**
 * Aptana Studio
 * Copyright (c) 2005-2011 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the GNU Public License (GPL) v3 (with exceptions).
 * Please see the license.html included with this distribution for details.
 * Any modifications to this file must keep this entire header intact.
 */
package com.aptana.git.ui.internal.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.osgi.util.NLS;

import com.aptana.git.core.model.GitRepository;
import com.aptana.git.ui.GitUIPlugin;
import com.aptana.git.ui.internal.Launcher;
import com.aptana.ui.MenuDialogItem;
import com.aptana.ui.QuickMenuDialog;

public class MergeBranchHandler extends AbstractGitHandler
{

	@Override
	protected Object doExecute(ExecutionEvent event) throws ExecutionException
	{
		final GitRepository repo = getSelectedRepository();
		if (repo == null)
		{
			return null;
		}

		String currentBranch = repo.currentBranch();
		List<MenuDialogItem> listOfMaps = new ArrayList<MenuDialogItem>();
		for (String branch : repo.allBranches())
		{
			if (branch.equals(currentBranch))
			{
				continue;
			}
			listOfMaps.add(new MenuDialogItem(branch));
		}
		if (!listOfMaps.isEmpty())
		{
			QuickMenuDialog dialog = new QuickMenuDialog(getShell());
			dialog.setInput(listOfMaps);
			if (dialog.open() != -1)
			{
				MenuDialogItem item = listOfMaps.get(dialog.getReturnCode());
				mergeBranch(repo, item.getText());
			}
		}
		return null;
	}

	public static void mergeBranch(final GitRepository repo, final String branchName)
	{
		Job job = new Job(NLS.bind("git merge {0}", branchName)) //$NON-NLS-1$
		{
			@Override
			protected IStatus run(IProgressMonitor monitor)
			{
				SubMonitor subMonitor = SubMonitor.convert(monitor, 100);

				repo.enterWriteProcess();
				try
				{
					ILaunch launch = Launcher.launch(repo, subMonitor.newChild(75), "merge", //$NON-NLS-1$
							branchName);
					while (!launch.isTerminated())
					{
						Thread.sleep(50);
					}
				}
				catch (CoreException e)
				{
					GitUIPlugin.logError(e);
					return e.getStatus();
				}
				catch (Throwable e)
				{
					GitUIPlugin.logError(e.getMessage(), e);
					return new Status(IStatus.ERROR, GitUIPlugin.getPluginId(), e.getMessage());
				}
				finally
				{
					repo.exitWriteProcess();
				}
				repo.index().refresh(subMonitor.newChild(25));
				return Status.OK_STATUS;
			}
		};
		job.setUser(true);
		job.setPriority(Job.LONG);
		job.schedule();
	}

}
