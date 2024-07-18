# Git Workflow

## Branches

Branches are a deviation or split from the main branch that can be adding or removing a specific feature
For example, I can open a branch to work on a new doc page for this training repo.
Since I am on my own branch, I am not interfering with the main branch's commit history, which is supposed to be kept clean.
A "clean" commit history is made up of large, well named commits to make it easy to quickly skim recent changes.
Because I am on my own branch, another student can also work on their own article without fear of interfering with my work.

To create a branch run:
`git branch -m "<branch name>"`

Then, this new branch works just like the main branch with `pull`, `add`, `commit`, and `push` commands.
You should see your current branch in blue in the command prompt if you are using git bash.
Otherwise you can use `git status` to check your branch.

Right now, this branch is only local to your computer.
To upload this branch to the remote repository so others can view it:
`git push --set-upstream origin <branch name>`

Once you have pushed the branch for the first time, it should show up on GitHub and be accessable by others.
You can and should use `git push` without the rest to push any further commits.

## Pull Requests

Pull requests and other organization practices are crucial to write maintainable, clean code

A pull request (PR) is a way to get review on changes before merging them to the main branch.
To make a pull request, first you need to have changes to merge.
These changes should be on their own, well named branch.
Then go to the pull requests tab on the github repo and hit `new pull request`.
You should be prompted to compare a branch with main.
Select the branch you want to merge.
You should see a list of changes and a green button that says `Create pull request`.
Click that button and then let a software lead or mentor know that you need review.

## Issues

Issues are a way to track features that we want to add and problems we need to fix in the repository.
Issues can be created by going to the issues tab of the repository on github and clicking `New issue`.
PRs can be linked to issues to show that an issue has been worked on and automatically resolve the issue when the PR is merged.
While our use of Issues is somewhat sporadic, they are useful for managing work.
If there is a relevant issue for something you are working on, make sure to keep it up to date!
If you have something that you think should be added or fixed, open an issue!

### Resources

- [Github creating a PR](https://docs.github.com/en/pull-requests/collaborating-with-pull-requests/proposing-changes-to-your-work-with-pull-requests/creating-a-pull-request)
- [Github about PRs](https://docs.github.com/en/pull-requests/collaborating-with-pull-requests/proposing-changes-to-your-work-with-pull-requests/about-pull-requests?platform=windows)
- [Github about issues](https://docs.github.com/en/issues/tracking-your-work-with-issues/about-issues)
- [Github linking a PR to an issue](https://docs.github.com/en/issues/tracking-your-work-with-issues/linking-a-pull-request-to-an-issue)

### Examples

- [A video demonstrating the exercise](../../Assets/PRDemoVideo.mkv)

### Exercises

- Create a branch with your name, add your name to the list below, and create a PR with that change.

### Names

- Lewy
- Athena
- Aliya
- Sneha
- Jerry

### Notes

- We use pull requests to make sure our code is readable and maintainable by having regular review of code.
