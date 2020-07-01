package org.ods.e2e.jira.helpers

class IssueSelectorHelper {
    static issueType = [
            releaseStatus             : 'release-status',
            technicalSpecificationTask: 'technical-specification-task',
            documentationChapter      : 'documentation-chapter',
            epic                      : 'epic',
            story                     : 'story',
            bug                       : 'bug',
            documentation             : 'documentation',
            test                      : 'test',
            mitigation                : 'mitigation',
    ]
}
